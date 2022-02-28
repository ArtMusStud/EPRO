package com.epro.ws2122.model;

import com.epro.ws2122.domain.KeyResultHistory;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Relation(collectionRelation = "keyResultHistory", value = "keyResultHistory")
public class KeyResultHistoryModel extends RepresentationModel<KeyResultHistoryModel> {

    private double oldCurrent;
    private double oldConfidence;
    private String comment;

    public KeyResultHistoryModel(KeyResultHistory krh) {
        this.oldCurrent = krh.getOldCurrent();
        this.oldConfidence = krh.getOldConfidence();
        this.comment = krh.getComment();
    }
}
