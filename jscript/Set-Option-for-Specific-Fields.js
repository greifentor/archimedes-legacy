load("nashorn:mozilla_compat.js");
importPackage(Packages.archimedes.scheme);

v = model.getAlleFelder();

for (j = 0, lenj = v.size(); j < lenj; j++) {
    sp = v.get(j);
    if (sp.getTable().getName().endsWith("_hist")) {
        if (sp.getName().startsWith("id_") || sp.getName().endsWith("_id")) {
            java.lang.System.out.println("found - " + sp.getFullName());
            sp.addOption(new Option("SUPPRESS_POTENTIAL_FK_WARNING"));
        }
    }
}
