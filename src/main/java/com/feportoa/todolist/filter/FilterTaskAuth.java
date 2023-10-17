package com.feportoa.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.feportoa.todolist.users.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//*
@Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserRepository userRepository; // Necessario para encontrar infos do usuario na etapa de validacao

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        var servletPath = request.getServletPath(); 
        // Se nao coletar o servletPath, vai fazer autenticacao para todas as requisições, aí não vai dar pra cadastrar usuários

        if(servletPath.startsWith("/tasks")){ // Filtra o servletPath só pras tasks
            // Pegar authorization (User e Password)
            var authorization = request.getHeader("Authorization");

            String authEncoded = authorization.substring("Basic".length()).trim(); // Retorna String criptografada
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded); // Transforma String em Base64
            String authString = new String(authDecoded); // Transforma em String legivel "uEmail:uPassword"

            String[] credentials = authString.split(":"); // Separa String "uEmail:uPassword" em 2 string
            String uEmail = credentials[0];
            String uPassword = credentials[1];

            // Validar usuário
            var user = this.userRepository.findByEmail(uEmail);
            
            if(user == null) {
                response.sendError(401);
            } else {
                // Validar senha do usuário
                var passwordVerify = BCrypt.verifyer().verify(uPassword.toCharArray(), user.getPassword());
                if(passwordVerify.verified) {
                    request.setAttribute("uId", user.getId()); // Cria o atributo uId contendo o id do usuario criado pra ser usado na controller task ou outras controllers 
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401);
                }
            }
        } else {
            filterChain.doFilter(request, response);
        }

    }
    
}
/**/