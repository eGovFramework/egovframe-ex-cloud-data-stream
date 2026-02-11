package egovframework.webflux.controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;

import egovframework.webflux.annotation.EgovController;
import egovframework.webflux.service.SampleService;
import egovframework.webflux.service.SampleVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@EgovController
@Slf4j
@RequiredArgsConstructor
public class SampleController {

    private final SampleService service;

    @GetMapping({"/","/sample/list"})
    public String list(@ModelAttribute SampleVO sampleVO, Model model) {
        IReactiveDataDriverContextVariable contextVariable =
                new ReactiveDataDriverContextVariable(this.service.list(), 1);
        model.addAttribute("resultList", contextVariable);
        return "egovSampleList";
    }

    @PostMapping("/sample/search")
    public String search(@ModelAttribute SampleVO sampleVO, Model model) {
        IReactiveDataDriverContextVariable contextVariable =
                new ReactiveDataDriverContextVariable(this.service.search(sampleVO), 1);
        model.addAttribute("resultList", contextVariable);
        return "egovSampleList";
    }

    @GetMapping("/sample/{id}")
    public String detail(@PathVariable int id, Model model) {
        Mono<SampleVO> detail = this.service.detail(id);
        model.addAttribute("sampleVO", detail);
        return "egovSampleRegister";
    }

    @GetMapping("/sample/add")
    public String form(@ModelAttribute SampleVO sampleVO) {
        return "egovSampleRegister";
    }

    @PostMapping("/sample/add")
    public String add(@ModelAttribute @Valid SampleVO sampleVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "egovSampleRegister";
        }
        this.service.add(sampleVO).subscribe();
        return "redirect:/";
    }

    @PostMapping("/sample/update")
    public String update(@ModelAttribute @Valid SampleVO sampleVO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "egovSampleRegister";
        }
        this.service.update(sampleVO).subscribe();
        return "redirect:/";
    }

    @DeleteMapping("/sample/{id}")
    public String delete(@PathVariable int id) {
        this.service.delete(id).subscribe();
        return "redirect:/";
    }

}
