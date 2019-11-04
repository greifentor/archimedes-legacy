load("nashorn:mozilla_compat.js");
importPackage(Packages.corent.db);
importPackage(Packages.archimedes.connections);

ldc.add(new DatabaseConnection("mySQL", "com.mysql.jdbc.Driver",
        "jdbc:mysql://SERVERNAME/DATABASE_NAME", "", DBExecMode.MYSQL, false, false, false, "")
        );
ldc.add(new DatabaseConnection("HypersonicSQL", "org.hsqldb.jdbcDriver",
        "jdbc:hsqldb:DATABASE_NAME", "sa", DBExecMode.HSQL, false, false, false, ""));
ldc.add(new DatabaseConnection("Oracle", "oracle.jdbc.driver.OracleDriver",
        "jdbc:oracle:thin:@SERVERNAME:PORT:DATABASE_NAME", "", DBExecMode.POSTGRESQL, false, true,
        true, ""));
ldc.add(new DatabaseConnection("PostgreSQL", "org.postgresql.Driver",
        "jdbc:postgresql://SERVERNAME/DATABASE_NAME", "", DBExecMode.POSTGRESQL, false, true,
        true, "\""));
