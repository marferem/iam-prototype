docker run -itd -p 389:389 --name openldap ^
    -e "LDAP_ADMIN_PASSWORD=admin" ^
    -e "LDAP_CONFIG_PASSWORD=config" ^
    osixia/openldap

