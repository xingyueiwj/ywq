package com.seeu.ywq.message.repository;

import com.seeu.ywq.message.model.PersonalMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by suneo.
 * User: neo
 * Date: 26/01/2018
 * Time: 4:41 PM
 * Describe:
 */

public interface PersonalMessageRepository extends JpaRepository<PersonalMessage, Long> {


    Page<PersonalMessage> findAllByUid(@Param("uid") Long uid,
                                       Pageable pageable);

    Page<PersonalMessage> findAllByUidAndType(@Param("uid") Long uid,
                                              @Param("type") PersonalMessage.TYPE type,
                                              Pageable pageable);

    Page<PersonalMessage> findAllByUidAndTypeNot(@Param("uid") Long uid,
                                                 @Param("type") PersonalMessage.TYPE type,
                                                 Pageable pageable);


    List<PersonalMessage> findAllByUidAndTypeNotAndCreateTimeAfter(@Param("uid") Long uid,
                                                                   @Param("type") PersonalMessage.TYPE type,
                                                                   @Param("createTime") Date createTime);

    List<PersonalMessage> findAllByUidAndTypeAndCreateTimeAfter(@Param("uid") Long uid,
                                                                @Param("type") PersonalMessage.TYPE type,
                                                                @Param("createTime") Date createTime);

    Integer countAllByUidAndTypeAndCreateTimeAfter(@Param("uid") Long uid,
                                                   @Param("type") PersonalMessage.TYPE type,
                                                   @Param("createTime") Date createTime);

    Integer countAllByUidAndTypeNotAndCreateTimeAfter(@Param("uid") Long uid,
                                                      @Param("type") PersonalMessage.TYPE type,
                                                      @Param("createTime") Date createTime);

    // update 2018-01-29
    List<PersonalMessage> findAllByUidAndTypeInAndCreateTimeAfterOrderByCreateTimeDesc(@Param("uid") Long uid,
                                                                                       @Param("types") Collection<PersonalMessage.TYPE> types,
                                                                                       @Param("createTime") Date createTime);

    Page<PersonalMessage> findAllByUidAndTypeInAndCreateTimeBefore(@Param("uid") Long uid,
                                                                                       @Param("types") Collection<PersonalMessage.TYPE> types,
                                                                                       @Param("createTime") Date createTime,
                                                                                       Pageable pageable);
}
