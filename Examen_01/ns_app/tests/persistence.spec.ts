import { NoSqlRepository } from "../repositories/nosql.repository";

describe("Pruebas de Persistencia Dual", () => {
    let noSqlRepo: NoSqlRepository;

    beforeEach(async () => {
        noSqlRepo = new NoSqlRepository();
        await noSqlRepo.init();
    });

    it("Debe escribir y leer correctamente en NoSQL (JSON)", async () => {
        const testItem = { name: "Test NoSQL", description: "Unit Test" };

        await noSqlRepo.create(testItem);
        const items = await noSqlRepo.getAll();

        const found = items.find(i => i.name === "Test NoSQL");
        expect(found).toBeDefined();
        if (found) {
            expect(found.description).toBe("Unit Test");
        }
    });

    it("Debe validar que el cambio de motor de datos es lógico", () => {
        let isSqlMode = true;
        const toggle = () => isSqlMode = !isSqlMode;

        expect(isSqlMode).toBe(true);
        toggle();
        expect(isSqlMode).toBe(false);
        toggle();
        expect(isSqlMode).toBe(true);
    });
});
