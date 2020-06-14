package com.sample.webclassroom.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.webclassroom.user.User;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

}

