package EvgenusT.SiteSharpening.Controllers;

import EvgenusT.SiteSharpening.Models.Post;
import EvgenusT.SiteSharpening.Repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ArticlesController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/articles")
    public String articlesMain(Model model) {
        Iterable<Post> posts = postRepository.findAll();
        model.addAttribute("posts", posts);
        return "articles";
    }

    // переход на страницу
    @GetMapping("/articles/add")
    public String articlesAdd(Model model) {
        return "articles-add";
    }

    //внесение записи в базу данных
    @PostMapping("/articles/add")
    public String articlePostAdd(@RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = new Post(title, anons, full_text);
        postRepository.save(post);
        return "redirect:/articles";
    }

    @GetMapping("/articles/{id}")
    public String articlesDetails(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/articles";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "articles-details";
    }

    @GetMapping("/articles/{id}/edit")
    public String articlesEdit(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/articles";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "articles-edit";
    }

    //редактирование записи в базе данных
    @PostMapping("/articles/{id}/edit")
    public String articlePostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/articles";
    }

    //удаление записи в базе данных
    @PostMapping("/articles/{id}/remove")
    public String articlePostDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/articles";
    }

}
