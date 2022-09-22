package com.finalproject.everrent_be.service;



import com.finalproject.everrent_be.dto.MemberResponseDto;
import com.finalproject.everrent_be.dto.ResponseDto;
import com.finalproject.everrent_be.exception.ErrorCode;
import com.finalproject.everrent_be.model.Authority;
import com.finalproject.everrent_be.model.Member;

import com.finalproject.everrent_be.model.Product;
import com.finalproject.everrent_be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;



    public ResponseDto<?> getMemberInfo(String email, HttpServletRequest request)
    {
        Member member = getMemberfromContext();
        if(member==null)
        {
            //여기 예외처리 global로 만들 생각중
        }
        //권한 확인을 어떻게 할지, 이 부분 확인 필요, 아마 SECURED로 처리하거나 security config에 바로 박아버릴지도
        Member findmember= findPresentMember(email);

        MemberResponseDto memberResponseDto= new MemberResponseDto(findmember);
        return ResponseDto.is_Success(memberResponseDto);
    }

    /*public ResponseDto<?> getMyPage()
    {
        Member member = getMemberfromContext();
        if(member==null)
        {
            ResponseDto.is_Fail(ErrorCode.NULL_TOKEN);
        }


    }*/



    public Member findPresentMember(String email)
    {
        Optional<Member> optionalMember=memberRepository.findByEmail(email);
        return optionalMember.orElse(null);
    }

    //여기서 컨텍스트 홀더에 저장된 username을 가져오는데
    //난 아까 member.getId()를 User객체로 만들어서 authentication으로 넣었기 때문에
    //컨텍스트 홀더에는 member의 id가 저장되어있고 우린 그걸 이용해서 멤버 객체를 찾는 것이다.
    @Transactional
    public Member getMemberfromContext() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findById(Long.valueOf(userId));  //Long.valueOf(userId)
        return member.get();
    }


}
