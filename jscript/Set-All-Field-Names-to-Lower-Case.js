/*
 * Sets all names to lowercase.
 *
 * @author O.Lieshoff
 *
 * @changed OLI (14.07.2024)
 */

l = model.getAllColumns();
for (i = 0, leni = l.length; i < leni; i++) {
    l[i].setName(l[i].getName().toLowerCase());
}

l = model.getAllDomains();
for (i = 0, leni = l.length; i < leni; i++) {
    l[i].setName(l[i].getName().toLowerCase());
}

l = model.getTables();
for (i = 0, leni = l.length; i < leni; i++) {
    l[i].setName(l[i].getName().toLowerCase());
}
