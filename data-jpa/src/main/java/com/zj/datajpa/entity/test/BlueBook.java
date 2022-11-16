package com.zj.datajpa.entity.test;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@DiscriminatorValue("blue")
public class BlueBook extends Book{

    private String blueTitle;
}
