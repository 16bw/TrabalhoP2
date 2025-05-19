package application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import application.model.Aluno;
import application.repository.AlunoRepository;


@Service
public class AppUserDataService implements UserDetailsService {
    @Autowired
    private AlunoRepository alunoRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Aluno aluno = alunoRepo.findByNome(username);

        if(aluno == null) {
            throw new UsernameNotFoundException("Aluno Não Encontrado");
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(aluno.getNome())
            .password(aluno.getSenha())
            .roles("USER")
            .build();

        return userDetails;
    }


}   