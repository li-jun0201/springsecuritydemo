package org.security.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by sang on 2017/1/10.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRole {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
