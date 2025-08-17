package com.mbilashobane.ai.mcp_core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import java.util.Arrays;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private Integer id;
    private String name;
    private String email;
    private String login;
    private String password;
    @JsonProperty("new_password")
    private String newPassword;
    @Builder.Default
    private Boolean active = true;
    @JsonProperty("active_partner")
    @Builder.Default
    private Boolean activePartner = true;
    @Builder.Default
    private Boolean share = true;
    @JsonProperty("companies_count")
    private Integer companiesCount;
    @JsonProperty("company_ids")
    private List<Integer> companyIds;
    @JsonProperty("notification_type")
    @Builder.Default
    private String notificationType = "email";
    @Builder.Default
    private String state = "new";
    @Builder.Default
    private String type = "contact";
    @Builder.Default
    private String phone = "false";
    @Builder.Default
    private String mobile = "false";
    @JsonProperty("is_company")
    @Builder.Default
    private Boolean isCompany = false;
    @JsonProperty("is_public")
    @Builder.Default
    private Boolean isPublic = true;
    @JsonProperty("groups_id")
    @Builder.Default
    private List<Integer> groupsId = Arrays.asList(11, 16);

    @JsonProperty("company_type")
    @Builder.Default
    private String companyType = "person";
    @JsonProperty("partner_share")
    @Builder.Default
    private Boolean partnerShare = true;
    @Builder.Default
    private String trust = "normal";
    @Builder.Default
    private String degree = "trip";
    @JsonProperty("work_exp")
    private String workExp;
    @Builder.Default
    private Boolean weekday = false;
    @JsonProperty("start_hour")
    @Builder.Default
    private Double startHour = 0.0;
    @JsonProperty("end_hour")
    @Builder.Default
    private Double endHour = 0.0;
    @JsonProperty("appoi_charge")
    private Double appoiCharge;
    @JsonProperty("slot_ids")
    private List<Integer> slotIds;
}
