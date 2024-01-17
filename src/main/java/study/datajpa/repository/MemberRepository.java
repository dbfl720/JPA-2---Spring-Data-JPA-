package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);


    // 실무에서 많이 쓰이는 기능!!
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();


    // dto로 조회할 경우는 new 다 써야줘야 됨. 실무에서 많이 쓰이는 기능!!
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t ")
    List<MemberDto> findMemberDto();


    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(Collection<String> names);


    List<Member> findListByUsername(String name); // 컬렉션

    Member findMemberByUsername(String username); // 단건

    Optional<Member> findOptionalByUsername(String username); // 단건 - optional


    // Slice<Member> findByAge(int age, Pageable pageable);
    @Query(value = "select m from Member m left join m.team t",
           countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable); // pageable : 쿼리 조건 (1페이지, 2페이지)

    @Modifying
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);


    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m ")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("team")) // entitygraph를 쓰면 fetchjoin을 편리하게 할 수 있다.
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    <T>List<T> findProjectionsByUsername(@Param("username") String username, Class<T> type);
}
