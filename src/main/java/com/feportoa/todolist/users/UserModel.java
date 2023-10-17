package com.feportoa.todolist.users;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Faz os getters e setters do class UserModel
@Entity(name = "tb_users") // Define o nome da tabela desses dados
public class UserModel {

    @Id // Define que eh um id (usando jakarta.persistence.Id)
    @GeneratedValue(generator = "UUID") // Gera um valor do tipo UUID
    private UUID id;

    private String email;
    private String password;
    private String username;
    private Integer horas;

    @CreationTimestamp // Cria um campo formatado como timestamp
    private LocalDateTime createdAt;
}
