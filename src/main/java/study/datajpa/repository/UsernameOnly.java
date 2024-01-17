package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {
    // open projection - entity를 다 가져와서 처리하는 것
   // @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
