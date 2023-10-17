package com.feportoa.todolist.task;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
// import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feportoa.todolist.utils.Utils;

import io.micrometer.core.ipc.http.HttpSender.Response;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;
    
    @PostMapping("")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest /* Toda vez que eu preciso que o user esteja autenticado, usar esse obj */ request) {
        var idUser = request.getAttribute("uId"); // Coleta o atributo passado na FilterTaskAuth.java como uId (Contem o UUID do usuário criado)
        taskModel.setUserId((UUID) idUser);

        LocalDateTime currentDate = LocalDateTime.now();
        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de início/término deve ser maior do que a data atual.");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("A data de início deve ser maior do que a de término");
        }

        TaskModel task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("")
    public List<TaskModel> list(HttpServletRequest request) 
    {
        var uId = request.getAttribute("uId");
        var tasks = this.taskRepository.findByUserId((UUID) uId);
        return tasks;
    }

    // http://localhost:8080/task/UUID-da-task
    // Da update em todas as infos, se nao tiver info fica como null
    @PutMapping("/{id}") // Task que quero alterar. Utilizando put + parametro
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable /* Path variable marca a variável que fica na URL */UUID id) 
    {
        var task = this.taskRepository.findById(id).orElse(null); // orElse coloca os valores vazios como null, pra evitar bugs/HTTP 500 

        if(task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Tarefa não encontrada.");
        }

        var userId = request.getAttribute("uId"); // Pega o id do usuario via atributo la do FilterTaskAuth.java

        if(!task.getUserId().equals(userId)) { // Se id do user for diferente do campo userId: 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Usuário não tem permissão para modificar este item.");
        }

        Utils.copyNonNullProperties(taskModel, task);

        return ResponseEntity.ok().body(this.taskRepository.save(task));
    }
}
