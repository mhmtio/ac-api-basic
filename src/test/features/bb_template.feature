Feature: Bloomberg attribute BB.TEMPLATE derives template correctly

  Scenario: BB ADO with Ado Type I and Product A
    Given an Ado with prefix BB
    And we set BB_FLW_ADO_TYPE to I
    And we set BB_FLW_PRODUCT to A
    When we read the value of BB.TEMPLATE
    Then we get BB+COM_LSA

  Scenario Outline: BB ADO with Ado Type and Product set
    Given an Ado with prefix BB
    And we set BB_RDR_FILENAME to <filename>
    And we set BB_ST006 to <actionType>
    And we set BB_FLW_ADO_TYPE to <adoType>
    And we set BB_FLW_PRODUCT to <product>
    When we read the value of BB.TEMPLATE
    Then we get <template>

    Examples:
      | adoType | product | filename | actionType | template          |
      | I       | A       |          |            | BB+COM_LSA        |
      | I       | B       |          |            | BB+COM_OP_LSA     |
      | I       | C       |          |            | BB+GCGC_LSA       |
      | I       | D       |          |            | BB+GCGC_LSA       |
      | I       | E       |          |            | BB+CUR_LSA        |
      | I       | F       |          |            | BB+EQY_OUT_LSA    |
      | I       | G       |          |            | BB+MF_LSA         |
      | I       | H       |          |            | BB+EQ_OP_LSA      |
      | I       | I       |          |            | BB+WAR_LSA        |
      | I       | J       |          |            | BB+GCGC_LSA       |
      | I       | K       |          |            | BB+MGT_AP_LSA     |
      | I       | M       |          |            | BB+GCGC_LSA       |
      | I       | N       |          |            | BB+EQY_OUT_LSA    |
      | I       | O       |          |            | BB+MGT_NP_LSA     |
      | I       | P       |          |            | BB+SYN_LSA        |
      | I       | Q       |          |            | BB+GCGC_LSA       |
      | I       | S       |          |            | BB+GCGC_LSA       |
      | I       | T       |          |            | BB+MGT_NP_LSA     |
      | I       | U       |          |            | BB+INDX_LSA       |
      | I       | V       |          |            | BB+MUNI_LSA       |
      | I       | X       |          |            | BB+MMKT_LSA       |
      | I       | W       |          |            | BB+GCGC_LSA       |
      | I       |         | mifid    |            | BB+MIFID_LSA      |
      | I       |         | bb_other |            | BB+PERSEC_LSA     |
      | IS      |         |          |            | BB+INST_LSA       |
      | C       |         |          | DELIST     | BB+EQY_DELIST_LSA |
      | C       |         |          | LIST       | BB+EQY_LIST_LSA   |
      | C       |         |          | OTHER      | BB+OTHER_LSA      |
      | H       |         |          |            | BB+PERSEC_LSA     |
