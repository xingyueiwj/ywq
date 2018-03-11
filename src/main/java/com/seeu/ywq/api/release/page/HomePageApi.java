package com.seeu.ywq.api.release.page;

import com.seeu.ywq.page_advertisement.model.Advertisement;
import com.seeu.ywq.page_advertisement.service.AdvertisementService;
import com.seeu.ywq.page_home.model.HomeUser;
import com.seeu.ywq.page_home.service.HomeUserService;
import com.seeu.ywq.user.service.UserPositionService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "APP页面数据主页接口", description = "首页四栏")
@RestController
@RequestMapping("/api/v1/page")
public class HomePageApi {
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private UserPositionService userPositionService;
    @Autowired
    private HomeUserService homeUserService;


    @ApiOperation(value = "首页广告")
    @GetMapping("/homepage/advertisements")
    public ResponseEntity homePageAdvertisements() {
        return ResponseEntity.ok(advertisementService.getAdvertisements(Advertisement.CATEGORY.HomePage));
    }

    @ApiOperation(value = "首页固定长度数据")
    @GetMapping("/homepage/youwu")
    public ResponseEntity homePage1(@AuthenticationPrincipal UserLogin authUser,
                                    @RequestParam(required = false) Integer size) {
        List<HomeUser> categoryList = null;
        if (authUser == null)
            categoryList = homeUserService.queryAll(HomeUser.LABEL.youwu, size);
        else
            categoryList = homeUserService.queryAll(authUser.getUid(), HomeUser.LABEL.youwu, size);
        return ResponseEntity.ok(categoryList);
    }


    @ApiOperation(value = "首页固定长度数据")
    @GetMapping("/homepage/hot-person")
    public ResponseEntity homePage2(@AuthenticationPrincipal UserLogin authUser, Integer size) {
        List<HomeUser> categoryList = null;
        if (authUser == null)
            categoryList = homeUserService.queryAll(HomeUser.LABEL.hotperson, size);
        else
            categoryList = homeUserService.queryAll(authUser.getUid(), HomeUser.LABEL.hotperson, size);
        return ResponseEntity.ok(categoryList);
    }


    @ApiOperation(value = "首页上拉加载数据")
    @GetMapping("/homepage/more")
    public ResponseEntity homePage3(@AuthenticationPrincipal UserLogin authUser,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        if (authUser != null)
            return ResponseEntity.ok(homeUserService.queryAll(authUser.getUid(), new PageRequest(page, size)));
        else
            return ResponseEntity.ok(homeUserService.queryAll(new PageRequest(page, size)));
    }


    @ApiOperation(value = "附近的人", notes = "（注：如果是已登陆用户，会将该用户的位置设定为传入的经纬度信息）需要传入：性别（可选，不填则表示所有），当前经纬度信息，测量距离，分页信息。分页默认：第 0 页，10 条。返回值中 distance 单位为：千米")
    @GetMapping("/homepage/nearby")
    public ResponseEntity getNearBy(@AuthenticationPrincipal UserLogin authUser,
                                    @RequestParam(required = false) UserLogin.GENDER gender,
                                    BigDecimal longitude,
                                    BigDecimal latitude,
                                    @RequestParam(defaultValue = "5") Long distance,
                                    @RequestParam(defaultValue = "0") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        Long uid = 0L;
        if (authUser != null) {
            userPositionService.updatePosition(authUser.getUid(), longitude, latitude);
            uid = authUser.getUid();
        }
        if (gender != null) {
            return ResponseEntity.ok(userPositionService.findNear(uid, gender, distance, longitude, latitude, new PageRequest(page, size)));
        }
        return ResponseEntity.ok(userPositionService.findNear(uid, distance, longitude, latitude, new PageRequest(page, size)));
    }


    @ApiOperation(value = "获取时首页所有数据", notes = "默认：推荐 2 条，附近的人 8 条，首页加载更多 10 条")
    @GetMapping("/homepage")
    public Map all(@AuthenticationPrincipal UserLogin authUser,
                   @RequestParam(required = false) UserLogin.GENDER gender,
                   BigDecimal longitude,
                   BigDecimal latitude,
                   @RequestParam(defaultValue = "5") Long distance) {
        Map map = new HashMap();
        map.put("advertisements", advertisementService.getAdvertisements(Advertisement.CATEGORY.HomePage));
        if (authUser == null) {
            map.put("youwu", homeUserService.queryAll(HomeUser.LABEL.youwu, 2));
            map.put("hotperson", homeUserService.queryAll(HomeUser.LABEL.hotperson, 2));
            map.put("more", homeUserService.queryAll(new PageRequest(0, 10)));
            if (gender != null)
                map.put("nearby", userPositionService.findNear(null, gender, distance, longitude, latitude, new PageRequest(0, 10)));
            else
                map.put("nearby", userPositionService.findNear(null, distance, longitude, latitude, new PageRequest(0, 10)));
        } else {
            map.put("youwu", homeUserService.queryAll(authUser.getUid(), HomeUser.LABEL.youwu, 2));
            map.put("hotperson", homeUserService.queryAll(authUser.getUid(), HomeUser.LABEL.hotperson, 2));
            map.put("more", homeUserService.queryAll(authUser.getUid(), new PageRequest(0, 10)));
            // 更新位置
            userPositionService.updatePosition(authUser.getUid(), longitude, latitude);
            if (gender != null)
                map.put("nearby", userPositionService.findNear(authUser.getUid(), gender, distance, longitude, latitude, new PageRequest(0, 10)));
            else
                map.put("nearby", userPositionService.findNear(authUser.getUid(), distance, longitude, latitude, new PageRequest(0, 10)));
        }
        return map;
    }
}
