package com.training.fileshare.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "usr")
@EqualsAndHashCode(of = { "id" })
@ToString(exclude = { "createdFiles", "recievedFiles" })
public class User implements UserDetails {

	private static final long serialVersionUID = -4557600846739433283L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String email;
	private String password;

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
	private List<TextFile> createdFiles = new ArrayList<>();

	@ManyToMany(mappedBy = "consumers", fetch = FetchType.LAZY)
	private List<TextFile> recievedFiles = new ArrayList<>();

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}
}
