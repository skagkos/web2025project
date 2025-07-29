/*
 * package diplomatiki.Controllers;
 * 
 * import diplomatiki.models.ThesisTopic;
 * import diplomatiki.Repositories.ThesisTopicRepository;
 * import org.springframework.beans.factory.annotation.Autowired;
 * import org.springframework.stereotype.Controller;
 * import org.springframework.ui.Model;
 * import org.springframework.web.bind.annotation.GetMapping;
 * 
 * import java.util.List;
 * 
 * @Controller
 * public class ProfessorTopicController {
 * 
 * @Autowired
 * private ThesisTopicRepository topicRepo;
 * 
 * @GetMapping("/didaskon/themata")
 * public String showTopics(Model model) {
 * List<ThesisTopic> topics = topicRepo.findAll();
 * model.addAttribute("topics", topics);
 * return "didaskon/themata";
 * }
 * }
 * /*
 */