package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        ArrayList<User> users = new ArrayList(); //storing multiple users
        ArrayList<Post> posts = new ArrayList(); //storing multiple posts

        Spark.staticFileLocation("/public"); //Spark serve public as static files
        Spark.init();
        Spark.post(
                "/create-user",
                ((request, response) -> {
                    User user = new User();
                    user.username = request.queryParams("username"); //sets the name
                    users.add(user);
                    response.redirect("/posts"); //redirects from /create-user to /posts page
                    return "";
                })
        );

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.post = request.queryParams("post");
                    posts.add(post);
                    response.redirect("/posts");
                    return "";
                })
        );

        Spark.get (
                "/posts",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("users", users); //this calls the {{#users}} and {{/users}} field
                    m.put("posts", posts);
                    return new ModelAndView(m, "posts.html");
                }),
                new MustacheTemplateEngine()
        );
    }
}

