package rastle.dev.rastle_backend.domain.hibernate.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.hibernate.application.HibernateTestService;
import rastle.dev.rastle_backend.domain.hibernate.dto.MemberInfo;
import rastle.dev.rastle_backend.domain.hibernate.dto.UpdateMember;

@RestController
@RequestMapping("/hibernate")
@RequiredArgsConstructor
public class HibernateTestApi {
    private final HibernateTestService hibernateTestService;
    
    @PostMapping("/member")
    public String addMember() {
        hibernateTestService.addMember();
        return "success";
    }

    @GetMapping("/members")
    public List<MemberInfo> getMembers() {
        return hibernateTestService.getMembers();
    }

    @GetMapping("/members/dto")
    public List<MemberInfo> getMembersDto() {
        return hibernateTestService.getMembersDto();
    }


    @GetMapping("/members/{id}")
    public MemberInfo getMember(@PathVariable("id") Long id) {
        return hibernateTestService.getMember(id);
    }

    @PutMapping("/members/{id}")
    public String updateMember(@RequestBody UpdateMember updateMember, @PathVariable("id") Long id) {
        hibernateTestService.updateMember(updateMember, id);
        return "success";
    }
}
