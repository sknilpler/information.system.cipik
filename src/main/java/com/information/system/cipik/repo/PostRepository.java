package com.information.system.cipik.repo;

import com.information.system.cipik.models.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends CrudRepository<Post,Long> {

    @Query(value = "select * from post where post_name like %:keyword%", nativeQuery = true)
    Iterable<Post> findAllByKeyword(@Param("keyword") String keyword);

    @Query(value = "SELECT post.* FROM post,employee WHERE employee.post_id = post.id and employee.id = :id_emp", nativeQuery = true)
    Post findByEmployeeId(@Param("id_emp") Long id);

    Post findByPostName(String postName);
}
