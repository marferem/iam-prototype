docker run -itd -p 6443:443 --network=my-ldap-network -e PHPLDAPADMIN_LDAP_HOSTS=172.19.0.2 --name ldapui  ^
    osixia/phpldapadmin
