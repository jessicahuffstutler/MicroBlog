package com.theironyard;

import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        ArrayList<Post> posts = new ArrayList(); //storing multiple posts

        Spark.staticFileLocation("/public"); //Spark serve public as static files
        Spark.init();

        Spark.post(
                "/create-user",
                ((request, response) -> {
                    String username = request.queryParams("username"); //sets the username
                    String password = request.queryParams("password"); //sets the password
                    Session session = request.session();
                    session.attribute("username", username); //store that username that the user types into the session (key, value)
                    session.attribute("password", password);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/create-post",
                ((request, response) -> {
                    Post post = new Post();
                    post.id = posts.size() + 1;
                    post.text = request.queryParams("text");
                    posts.add(post);
                    response.redirect("/");
                    return "";
                })
        );

        Spark.get( //takes 3 arguments: "/posts" now -> "/",, ((request, response) -> {, new MustacheTemplateEngine()
                "/",
                ((request, response) -> {
                    Session session = request.session(); //method called subject that gives you an object "session"
                    String username = session.attribute("username");
                    if (username == null) { //if the username doesn't exist in the hashmap yet, add it.
                        return new ModelAndView(new HashMap(), "not-logged-in.html"); //new HashMap() = an empty hashmap. This is empty because we dont need to feed anything into it because it's a get not a post.
                    }
                    HashMap m = new HashMap();
                    m.put("username", username); //"username" calls the {{username}}
                    m.put("posts", posts); //"posts" calls the {{#posts}} and {{/posts}} field
                    return new ModelAndView(m, "logged-in.html"); //this wants 2 things: your hashmap with your pair values in it (m.put) and the name of the template (logged-in.html).
                }),
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/delete-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try {
                        int idNum = Integer.valueOf(id);
                        posts.remove(idNum - 1);
                        for (int i = 0; i < posts.size(); i++) { //renumbering
                            posts.get(i).id = i + 1; //renumbering
                        }
                    } catch (Exception e) {

                    }
                    response.redirect("/");
                    return "";
                })
        );

        Spark.post(
                "/edit-post",
                ((request, response) -> {
                    String id = request.queryParams("postid");
                    try {
                        int idNum = Integer.valueOf(id);
                        posts.get(idNum - 1).text = request.queryParams("text"); //replaces same number with edited text
                        for (int i = 0; i < posts.size(); i++) { //renumbering
                            posts.get(i).id = i + 1; //renumbering
                        }
                    } catch (Exception e) {

                    }
                    response.redirect("/");
                    return "";
                })
        );

//Zach's Code
//        Spark.post(
//                "/edit-post",
//                ((request, response) -> {
//                    Session session = request.session();
//                    String name = session.attribute("name");
//
//                    String id = request.queryParams("postid");
//                    String text = request.queryParams("text");
//                    try {
//                        int idNum = Integer.valueOf(id);
//                        ArrayList<Post> posts = users.get(name).posts; //looking up the post in the ArrayList
//                        Post post = posts.get(idNum -1);
//                        post.text = text; //that post will have an updated text field
//                        //users.get(name).posts.get(idNum-1).text = text; //alex's code(instead of 3 lines above.)
//                    } catch (Exception e) {
//
//                    }
//                    response.redirect("/");
//                    return "";
//                })
//        );
    }
}

