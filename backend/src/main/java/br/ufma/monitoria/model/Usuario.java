package br.ufma.monitoria.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements  UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nome;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(unique = true, nullable = false)
    private String email;

    private String telefone;

    private String cpf;

    private String matricula;

    private String curso;


        @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // você pode retornar uma lista fixa ou dinâmica de perfis
        return Collections.singleton(() -> "ROLE_USER");
    }

    @Override
    public String getPassword() {
        return this.senha; // ou como você nomeou o campo da senha
    }

    @Override
    public String getUsername() {
        return this.email; // ou nome de usuário, dependendo do projeto
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // ou lógica real
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Column(nullable = false)
    @JsonIgnore
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipo;

    @Column(name = "codigo_verificacao")
    private String codigoVerificacao;

    // Relacionamentos
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    private List<SessaoMonitoria> sessoes;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL)
    private List<MensagemChat> mensagens;

    @OneToMany(mappedBy = "monitor", cascade = CascadeType.ALL)
    private List<Monitoria> monitorias;
}
