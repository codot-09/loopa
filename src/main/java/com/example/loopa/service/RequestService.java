package com.example.loopa.service;

import com.example.loopa.dto.ApiResponse;
import com.example.loopa.dto.request.SellerRequest;
import com.example.loopa.dto.response.RequestedSellerResponse;
import com.example.loopa.entity.Request;
import com.example.loopa.entity.User;
import com.example.loopa.entity.enums.Role;
import com.example.loopa.exception.DataNotFoundException;
import com.example.loopa.repository.RequestRepository;
import com.example.loopa.repository.UserRepository;
import com.example.loopa.service.bot.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final BotService botService;

    public ApiResponse<String> createRequest(User user, SellerRequest request){

        if (requestRepository.existsBySeller(user)){
            return ApiResponse.fail("Ariza yuborilgan");
        }

        Request request1 = Request.builder()
                .seller(user)
                .name(request.getName())
                .sellerInfo(request.getSellerInfo())
                .contact(request.getContact())
                .build();

        requestRepository.save(request1);

        return ApiResponse.success("Ariza yuborildi");
    }

    @Transactional
    public ApiResponse<String> makeSeller(UUID requestId){
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException("Ariza topilmadi"));

        User user = request.getSeller();

        user.setRole(Role.SELLER);
        userRepository.save(user);

        requestRepository.delete(request);

        String welcomeMessage = String.format(
                "<b>Tabriklaymiz !</b> 🎉\n\n" +
                        "Sizning <b>Loopa Market</b> platformasidagi sotuvchilik arizangiz muvaffaqiyatli tasdiqlandi. " +
                        "Endi siz o'z mahsulotlaringizni joylashtirishingiz va savdoni boshlashingiz mumkin. 🚀\n\n" +
                        "<i>Sizga barakali savdo va ko'plab mijozlar tilaymiz!</i>\n\n" +
                        "Panelga o'tish uchun quyidagi tugmani bosing: 👇"
        );

        botService.sendMessage(Long.parseLong(user.getChatId()), welcomeMessage);

        return ApiResponse.success("O'zgarishlar saqlandi", null);
    }

    public ApiResponse<List<RequestedSellerResponse>> getRequests(){
        List<Request> requests = requestRepository.findAll();

        return ApiResponse.success(null,requests.stream().map(this::mapToResponse).toList());
    }

    private RequestedSellerResponse mapToResponse(Request request){
        return RequestedSellerResponse.builder()
                .requestId(request.getId())
                .name(request.getName())
                .sellerInfo(request.getSellerInfo())
                .contact(request.getContact())
                .build();
    }
}
