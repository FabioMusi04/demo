package me.demo.auditable;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import me.demo.user.User;

public class AuditAware implements AuditorAware<Long>{

  @Override
  public Optional<Long> getCurrentAuditor() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      return Optional.empty();
    }

    User userPrincipal = (User) authentication.getPrincipal();
    return Optional.ofNullable(userPrincipal.getId());
  }
}
