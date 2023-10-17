package com.feportoa.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/*
 * Funções da ToDo List
 *  
 * ID
 * Usuário (ID do usuário)
 * Descrição
 * Título
 * Data de início
 * Data de término
 * Prioridade
 * 
 */

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    @Column(length = 50) // Define o tamanho máximo do title para 50 caractéres
    private String title;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String priority;
    
    private UUID userId;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setTitle(String title) throws Exception { // Tem que passar um throws Exception para que o método lide com o problema
        if(title.length() > 50){
            throw new Exception("O campo title deve conter no máximo 50 caractéres!");
        }
        this.title = title;
    }
}
