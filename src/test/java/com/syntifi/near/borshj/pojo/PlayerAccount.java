package com.syntifi.near.borshj.pojo;

import java.util.Arrays;

import com.syntifi.near.borshj.Borsh;
import com.syntifi.near.borshj.annotation.BorshField;

/**
 * Test pojo for github issue #4 (https://github.com/near/borshj/issues/4)
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
public class PlayerAccount implements Borsh {

    @SuppressWarnings("DefaultAnnotationParam")
    @BorshField(1)
    public byte[] owner = new byte[32];

    @BorshField(2)
    public byte[] nft_addr = new byte[32];

    @BorshField(3)
    public byte is_waiting_match_making;

    @BorshField(4)
    public byte is_waiting_battle_royal;

    public PlayerAccount() {
    }

    public PlayerAccount(byte[] owner, byte[] nft_addr, byte is_waiting_match_making, byte is_waiting_battle_royal) {
        this.owner = owner;
        this.nft_addr = nft_addr;
        this.is_waiting_battle_royal = is_waiting_battle_royal;
        this.is_waiting_match_making = is_waiting_match_making;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + is_waiting_battle_royal;
        result = prime * result + is_waiting_match_making;
        result = prime * result + Arrays.hashCode(nft_addr);
        result = prime * result + Arrays.hashCode(owner);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlayerAccount other = (PlayerAccount) obj;
        if (is_waiting_battle_royal != other.is_waiting_battle_royal)
            return false;
        if (is_waiting_match_making != other.is_waiting_match_making)
            return false;
        if (!Arrays.equals(nft_addr, other.nft_addr))
            return false;
        return Arrays.equals(owner, other.owner);
    }

}
