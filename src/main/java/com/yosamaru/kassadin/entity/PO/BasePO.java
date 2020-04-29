package com.yosamaru.kassadin.entity.PO;

import java.sql.Timestamp;
import java.util.EnumSet;
import javax.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePO {

  public enum Status {
    FREEZE("FREEZE"),
    THAW("THAW");

    private final String value;

    @Override
    public String toString() {
      return value;
    }

    private Status(final String value) {
      this.value = value;
    }

    public static BasePO.Status resolve(final String value) {
      for (final BasePO.Status s : EnumSet.allOf(BasePO.Status.class)) {
        if (s.toString().equals(value)) {
          return s;
        }
      }
      return null;
    }
  }

  @Column(name = "STATUS")
  private String status;

  @Column(name = "create_time")
  private Timestamp createTime;

  @Column(name = "update_time")
  private Timestamp updateTime;
}