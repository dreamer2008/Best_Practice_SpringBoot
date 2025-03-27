package com.tom.bp.springboot.jpa.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

//@ApiModel("Employee")
@Entity
@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Employee implements Serializable {

//    @ApiModelProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
//    @JsonIgnore
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

//    @ApiModelProperty(name = "Email", required = true)
    @Column(name = "email", nullable = false)
//    @NotBlank(message = "Email cannot be empty", groups = UserAddGroup.class)
    private String email;

//    @ApiModelProperty(name = "State(Validity)", example = "1: valid, 0: invalid")
    private int state;

//    @ApiModelProperty(name = "Create Time")
    @Column(name = "create_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

//    @ApiModelProperty(value = "Update Time")
    @Column(name = "update_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateAt;

}