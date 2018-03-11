package com.seeu.ywq.api.release.user;

import com.seeu.core.R;
import com.seeu.ywq.user.dvo.PhotoWallVO;
import com.seeu.ywq.user.service.UserPhotoWallService;
import com.seeu.ywq.userlogin.model.UserLogin;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 查看个人照片墙信息
 */
@Api(tags = "用户照片墙", description = "用户照片墙信息查看/修改")
@RestController
@RequestMapping("/api/v1/user")
public class UserPhotoWallApi {

    @Autowired
    private UserPhotoWallService userPhotoWallService;

    /**
     * 查看用户照片墙信息【本人】
     *
     * @param authUser
     * @return
     */
    @ApiOperation(value = "查看用户照片墙信息【本人】", notes = "查看用户自己的照片墙信息（最多5张），用户需要处于已登陆状态")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功")
    })
    @GetMapping("/photo-wall")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity getMineAlbumWall(@AuthenticationPrincipal UserLogin authUser) {
        List<PhotoWallVO> albumWalls = userPhotoWallService.findAllByUid(authUser.getUid());
        return ResponseEntity.ok(albumWalls);
    }

    @ApiOperation(value = "删除照片（单张）")
    @DeleteMapping("/photo-wall/{id}")
    public ResponseEntity deleteByUidAndId(@AuthenticationPrincipal UserLogin authUser,
                                           @PathVariable Long id) {
        if (!userPhotoWallService.exist(authUser.getUid(), id))
            return ResponseEntity.status(404).body(R.code(404).message("无此照片，无法删除"));
        userPhotoWallService.delete(authUser.getUid(), id);
        return ResponseEntity.ok(R.code(200).message("删除成功"));
    }

    @ApiOperation(value = "删除照片（多张）")
    @DeleteMapping("/photo-wall")
    public ResponseEntity deleteByUidAndIds(@AuthenticationPrincipal UserLogin authUser,
                                            @RequestParam Long[] ids) {
        if (ids == null || ids.length == 0)
            return ResponseEntity.badRequest().body(R.code(4001).message("传入参数不能为空"));
        userPhotoWallService.delete(authUser.getUid(), ids);
        return ResponseEntity.ok(R.code(200).message("删除成功"));
    }

    /**
     * 查看用户照片墙信息
     *
     * @param uid
     * @return
     */
    @ApiOperation(value = "查看用户照片墙信息", notes = "查看用户其他人的照片墙信息（最多5张）")
    @ApiResponses({
            @ApiResponse(code = 200, message = "查询成功")
    })
    @GetMapping("/{uid}/photo-wall")
    public ResponseEntity getAlbumWall(@PathVariable("uid") Long uid) {
        List<PhotoWallVO> albumWalls = userPhotoWallService.findAllByUid(uid);
        return ResponseEntity.ok(albumWalls);
    }

    @ApiOperation(value = "添加用户照片墙信息【本人】", notes = "添加用户自己的照片墙信息（一次一组，最多5张），用户需要处于已登陆状态")
    @ApiResponses({
            @ApiResponse(code = 201, message = "上传成功"),
            @ApiResponse(code = 400, message = "400 数据错误"),
            @ApiResponse(code = 500, message = "500 服务器异常")
    })
    @PostMapping("/photo-wall")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity add(@AuthenticationPrincipal UserLogin authUser,
                              MultipartFile[] images) {
        if (images == null || images.length == 0)
            return ResponseEntity.badRequest().body(R.code(4001).message("请传入至少一张图片").build());
        int maxSize = 6;
        // 查看已有多少张，看是否传入超额，最多 5 张
        if (images.length > maxSize)
            return ResponseEntity.badRequest().body(R.code(4002).message("传入图片过多，请不要超过 " + maxSize + " 张").build());
        if (userPhotoWallService.countExistPhotos(authUser.getUid()) + images.length > maxSize)
            return ResponseEntity.badRequest().body(R.code(4003).message("传入图片过多，照片墙上照片数总量不能超过 " + maxSize + " 张").build());
        try {
            List<PhotoWallVO> photoWallVOS = userPhotoWallService.saveImages(authUser.getUid(), images);
            return photoWallVOS.size() == 0 ? ResponseEntity.badRequest().body(R.code(4001).message("请至少传入一张图片").build()) : ResponseEntity.status(201).body(photoWallVOS);
        } catch (Exception e) {
            // 注意回滚（如果异常，阿里云可能会存储部分图片，但本地可能无对应图片信息）
            return ResponseEntity.status(500).body(R.code(500).message("服务器异常，文件传输失败").build());
        }
    }
}
