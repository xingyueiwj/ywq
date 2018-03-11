package com.seeu.ywq.api.release.page;

import com.seeu.ywq.trend_lite.service.AppPublishPageService;
import com.seeu.ywq.page_advertisement.model.Advertisement;
import com.seeu.ywq.page_advertisement.service.AdvertisementService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "APP页面数据动态", description = "推荐/关注/六则广告/我的（或其他用户的）动态列表")
@RestController
@RequestMapping("/api/v1/page/publish")
public class PagePublishApi {

    @Autowired
    private AppPublishPageService appPublishPageService;
    @Autowired
    private AdvertisementService advertisementService;

    @ApiOperation(value = "广告（至少会有两条）", notes = "至少两条，一般六条，一次请求会返回所有信息")
    @GetMapping("/advertisements")
    public ResponseEntity getAds() {
        List list = advertisementService.getAdvertisements(Advertisement.CATEGORY.PublishPage);
        return ResponseEntity.ok(list);
    }

    @ApiOperation(value = "推荐【需要登录】", notes = "根据用户信息（关注标签）进行推荐，若匿名用户，则默认表示所有标签。按时间排序")
    @GetMapping("/recommends")
//    @PreAuthorize("hasRole('USER')") // 人人可访问
    public ResponseEntity getTuijian(@AuthenticationPrincipal UserLogin authUser,
                                     @RequestParam(defaultValue = "0") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        // TODO 可以个性化推荐（根据标签？）
        Long uid = null; // 要么 null，要么正确数值
        if (authUser != null)
            uid = authUser.getUid();
        return ResponseEntity.ok(appPublishPageService.getTuijian(uid, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime"))));
    }


    @ApiOperation(value = "关注【需要登录】", notes = "列出个人关注的用户的动态列表")
    @GetMapping("/follows")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getGuangzhu(@AuthenticationPrincipal UserLogin authUser,
                                      @RequestParam(defaultValue = "0") Integer page,
                                      @RequestParam(defaultValue = "10") Integer size) {
        Long uid = 0L;
        if (authUser != null)
            uid = authUser.getUid();
        return ResponseEntity.ok(appPublishPageService.getGuanzhu(uid, new PageRequest(page, size)));
    }

    @ApiOperation(value = "我的所有动态【需要登录】", notes = "列出个人用户的动态列表")
    @GetMapping("/mine")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMines(@AuthenticationPrincipal UserLogin authUser,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(appPublishPageService.getWhose(authUser.getUid(), authUser.getUid(), new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime"))));
    }

    @ApiOperation(value = "用户所有动态", notes = "列出个人用户的动态列表")
    @GetMapping("/user/{uid}")
    public ResponseEntity getWhose(@AuthenticationPrincipal UserLogin authUser,
                                   @PathVariable("uid") Long uid,
                                   @RequestParam(defaultValue = "0") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size) {
        Long visitorUid = 0L;
        if (authUser != null) visitorUid = authUser.getUid();
        return ResponseEntity.ok(appPublishPageService.getWhose(visitorUid, uid, new PageRequest(page, size, new Sort(Sort.Direction.DESC, "createTime"))));
    }
}
