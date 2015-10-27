package com.theironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        User user = new User();

        ArrayList<User> users = new ArrayList(); //storing multiple users
        ArrayList<Post> posts = new ArrayList(); //storing multiple posts

        Spark.staticFileLocation("/public"); //Spark serve public as static files
        Spark.init();

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    user.username = request.queryParams("username"); //sets the name
                    user.password = request.queryParams("password");
                    users.add(user);
                    response.redirect("/posts"); //redirects from /create-user to /posts page
                    return "";
                })
        );

        //Below is a failed attempt to get the password field to function
        //would replace "/create-user" above

//        Spark.post(
//                "/create-user",
//                ((request, response) -> {
//                    HashMap<String, String> logins = new HashMap();
//                    user.username = request.queryParams("username"); //sets the username
//                    user.password = request.queryParams("password"); //sets the password
//                    users.add(user);
//                    String pWord = user.password;
//                    String uName = user.username;
//                    logins.put("username", username); //trying to put username in the hashmap
//                    logins.put("password", password); //trying to put password in the hashmap
//                    if (user.password == pWord) {  //hashmap addition
//                        response.redirect("/posts"); //redirects from /create-user to /posts page
//                        return "";
//                    } else
//                        response.redirect("/");
//                        return "";
//                })
//        );

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.text = request.queryParams("text");
                    posts.add(post);
                    response.redirect("/posts");
                    return "";
                })
        );

        Spark.get( //takes 3 arguments: "/posts",, ((request, response) -> {, new MustacheTemplateEngine()
                "/posts",
                ((request, response) -> {
                    HashMap m = new HashMap();
                    m.put("username", user.username); //"username" calls the {{username}}
                    m.put("posts", posts); //"posts" calls the {{#posts}} and {{/posts}} field
                    return new ModelAndView(m, "posts.html"); //this wants 2 things: your hashmap with your pair values in it (m.put) and the name of the template (posts.html).
                }),
                new MustacheTemplateEngine()
        );
    }
}

