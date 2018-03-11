package com.seeu.ywq.trend.service;

import com.seeu.ywq.api.admin.trend.PUBLISH;
import com.seeu.ywq.exception.*;
import com.seeu.ywq.trend.dvo.PublishVO;
import com.seeu.ywq.trend.model.Publish;
import com.seeu.ywq.trend.model.PublishComment;
import com.seeu.ywq.trend.model.PublishLikedUser;
import com.seeu.ywq.userlogin.model.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;

public interface PublishService {

    ///////////////////////////////////////////************** basic curd operations ***************////////////////////////////////////////////////////

    Publish findOne(Long publishId);

    Publish findOne(Long uid, Long publishId);


    boolean exist(Long publishId);

    @Transactional
    Publish save(Publish publish);

    Publish saveWithImage(Publish publish);

    ///////////////////////////////////////////************** vo query operations ***************////////////////////////////////////////////////////

    Page findAllByUid(Long uid, boolean canVisitClosedResource, Pageable pageable);

    PublishVO findOneByPublishId(Long publishId, boolean canVisitClosedResource);

    ///////////////////////////////////////////************** other operations ***************////////////////////////////////////////////////////

    /* 基于 findOneByPublishId(publishId,uid) */
    PublishVO viewIt(Long publishId, Long uid);

    /* 基于 findOneByPublishId(publishId) */
    PublishVO viewIt(Long publishId);

    // 一并删除评论、点赞信息等（用户发布数量减一）
    void deletePublish(Long publishId) throws ResourceNotFoundException;

    Page<PublishLikedUser> listLikedUser(Long publishId, Pageable pageable);

    void likeIt(Long publishId, UserLogin user) throws ResourceNotFoundException, ActionNotSupportException, ResourceAlreadyExistedException;

    void dislikeIt(Long publishId, Long uid) throws ResourceNotFoundException, ActionNotSupportException;

    PublishComment getComment(Long commentId);

    Page<PublishComment> listComments(Long publishId, Pageable pageable);

    void commentIt(Long publishId, Long fatherId, UserLogin user, String text) throws ResourceNotFoundException, ActionNotSupportException;

    void deleteComment(Long commentId) throws ResourceNotFoundException;

    // ... 获取解锁金额
    Long getUnlockDiamonds(Long publishId) throws ResourceNotFoundException, PublishTYPENotAllowedException;


    // admin //
    Page<Publish> findAll(Pageable pageable);

    Page<Publish> searchAll(PUBLISH search, String word, Pageable pageable) throws ActionParameterException;

    Publish getOne(Long publishId) throws ResourceNotFoundException;

    void complaint(UserLogin user, Long publishid) throws ResourceNotFoundException, ActionNotSupportException;


}
