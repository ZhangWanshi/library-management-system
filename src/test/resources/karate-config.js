function fn() {

    var port = karate.properties['karate.port'];

    var config = {
        baseUrl: 'http://localhost:' + port
    };

    // login once
    var result = karate.callSingle('classpath:karate/utils/auth.feature', config);

    config.adminToken = result.tokens.admin;
    config.librarianToken = result.tokens.librarian;
    config.memberToken = result.tokens.member;

    return config;
}