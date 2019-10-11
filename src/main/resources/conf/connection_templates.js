importPackage(Packages.corent.db);
importPackage(Packages.archimedes.connections);

ldc.add(new DatabaseConnection("mySQL", "com.mysql.jdbc.Driver",
        "jdbc:mysql://SERVERNAME/DATABASE_NAME", "", DBExecMode.MYSQL, false, false, false, "")
        );
ldc.add(new DatabaseConnection("HypersonicSQL", "org.hsqldb.jdbcDriver",
        "jdbc:hsqldb:DATABASE_NAME", "", DBExecMode.HSQL, false, false, false, ""));
ldc.add(new DatabaseConnection("PostgreSQL", "org.postgresql.Driver",
        "jdbc:postgresql://SERVERNAME/DATABASE_NAME", "op1", DBExecMode.POSTGRESQL, false, true,
        true, "\""));
