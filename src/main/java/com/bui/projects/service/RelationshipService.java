package com.bui.projects.service;

import java.util.List;

public interface RelationshipService {

    List<Integer> getRelationships(Integer peronId);

    List<Integer> getParents(Integer peronId);
    List<Integer> getKids(Integer peronId);
    List<Integer> getSiblings(Integer peronId);
    List<Integer> getSpouses(Integer peronId);

    List<Integer> getFathers(Integer peronId);
    List<Integer> getMothers(Integer peronId);
    List<Integer> getSons(Integer peronId);
    List<Integer> getDaughters(Integer peronId);
    List<Integer> getBrothers(Integer peronId);
    List<Integer> getSisters(Integer peronId);
    List<Integer> getHusbands(Integer peronId);
    List<Integer> getWifies(Integer peronId);

    boolean createRelationship(Integer peronId, Integer relatedPersonId, Integer relationType);

}
