package com.yuanwei.resistance.partygame.avalon.rule;

import com.yuanwei.resistance.constant.Constants;
import com.yuanwei.resistance.partygame.avalon.model.Avalon.Role;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by chenyuanwei on 15/10/10.
 */
public class SetupRule {

    private Set<Role> mordred_party;
    private Set<Role> arthur_party;

    public SetupRule() {
        mordred_party = new HashSet<>();
        arthur_party = new HashSet<>();
    }

    public synchronized boolean isRoleSelectable(Role role, int limit) {
        boolean result = true;

        // We do not need to worry numbers of arthur party.
        final int mordred_party_size_limit = Constants.getSpyPlayers(limit);
        int expected_size = mordred_party.size();
        switch (role) {
            case MERLIN:
                if (!mordred_party.contains(Role.ASSASSIN)) expected_size ++;
                break;
            case ASSASSIN:    
                if (!mordred_party.contains(Role.ASSASSIN)) expected_size ++;
                break;
            case PERCIVAL:
                result = result && arthur_party.contains(Role.MERLIN);
                break;
            case MORDRED:
                result = result && arthur_party.contains(Role.MERLIN);
                if (!mordred_party.contains(role)) expected_size++;
                break;
            case OBERON:
                if (!mordred_party.contains(role)) expected_size++;
                break;
            case MORGANA:
                if (!mordred_party.contains(role)) expected_size++;
                result = result
                        && arthur_party.contains(Role.MERLIN)
                        && arthur_party.contains(Role.PERCIVAL);
                break;
            case LANCELOT_ARTHUR:
            case LANCELOT_MORDRED:    
                if (!mordred_party.contains(Role.LANCELOT_MORDRED)) expected_size++;
                break;
        }
        result = expected_size <= mordred_party_size_limit && result;
        return result;
    }

    public boolean isRoleSelected(Role role) {
        if (role.getRoleId() > 0)
            return arthur_party.contains(role);
         else
            return mordred_party.contains(role);
    }

    public void notifyRoleSelected(Role role) {
        if (role == Role.MERLIN || role == Role.ASSASSIN) {
            arthur_party.add(Role.MERLIN);
            mordred_party.add(Role.ASSASSIN);
        } else if (role == Role.LANCELOT_ARTHUR
                || role == Role.LANCELOT_MORDRED) {
            arthur_party.add(Role.LANCELOT_ARTHUR);
            mordred_party.add(Role.LANCELOT_MORDRED);
        } else if (role == Role.PERCIVAL) {
            arthur_party.add(role);
        } else {
            mordred_party.add(role);
        }
    }

    public void notifyRoleRemoved(Role role) {
        if (role == Role.MERLIN || role == Role.ASSASSIN) {
            arthur_party.remove(Role.MERLIN);
            mordred_party.remove(Role.ASSASSIN);
        } else if (role == Role.LANCELOT_ARTHUR
                || role == Role.LANCELOT_MORDRED) {
            arthur_party.remove(Role.LANCELOT_ARTHUR);
            mordred_party.remove(Role.LANCELOT_MORDRED);
        } else if (role == Role.PERCIVAL) {
            arthur_party.remove(role);
        } else {
            mordred_party.remove(role);
        }
    }

    public void reset() {
        mordred_party.clear();
        arthur_party.clear();
    }
}
