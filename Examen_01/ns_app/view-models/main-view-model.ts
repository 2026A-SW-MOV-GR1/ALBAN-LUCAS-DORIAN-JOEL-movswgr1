import { Observable, ObservableArray, Switch, Application } from "@nativescript/core";
import { Item } from "../models/item.model";
import { SqliteRepository } from "../repositories/sqlite.repository";
import { NoSqlRepository } from "../repositories/nosql.repository";
import { IRepository } from "../models/repository.interface";
import { Logger } from "../utils/logger";
import { UIHelper } from "../utils/ui-helper";

export class MainViewModel extends Observable {
    private sqlRepo = new SqliteRepository();
    private noSqlRepo = new NoSqlRepository();
    private currentRepo: IRepository;

    public items = new ObservableArray<Item>();
    public newItemName: string = "";
    public isSqlMode: boolean = true;
    public isDarkMode: boolean = false;
    public isEditing: boolean = false;
    public editingItemId: number | string | undefined = undefined;
    public engineLabel: string = "SQL Engine";
    public submitButtonText: string = "+";

    constructor() {
        super();
        this.currentRepo = this.sqlRepo;
        this.init().catch(err => Logger.error("Error en init VM", err));
    }

    async init() {
        await this.sqlRepo.init();
        await this.noSqlRepo.init();
        await this.refresh();
    }

    onToggleEngine = async (args: any) => {
        const sw = args.object as Switch;
        if (this.isSqlMode === sw.checked) return;

        this.set("isSqlMode", sw.checked);
        this.currentRepo = this.isSqlMode ? this.sqlRepo : this.noSqlRepo;

        const label = this.isSqlMode ? "SQL Engine" : "NoSQL Engine";
        this.set("engineLabel", label);

        Logger.info(`Motor cambiado a: ${this.isSqlMode ? 'SQL' : 'NoSQL'}`);
        UIHelper.showToast(`Cambiado a ${this.isSqlMode ? 'SQLite' : 'JSON'}`);
        await this.refresh();
    }

    onToggleDarkMode = (args: any) => {
        const sw = args.object as Switch;
        this.set("isDarkMode", sw.checked);
        const rootLayout = Application.getRootView();
        if (this.isDarkMode) {
            rootLayout.className = "ns-dark";
        } else {
            rootLayout.className = "ns-light";
        }
    }

    addItem = async () => {
        if (!this.newItemName || this.newItemName.trim() === "") return;

        if (this.isEditing && this.editingItemId !== undefined) {
            const updatedItem: Item = {
                id: this.editingItemId,
                name: this.newItemName,
                description: `Editado - Persistencia: ${this.isSqlMode ? 'SQL' : 'NoSQL'}`
            };
            await this.currentRepo.update(updatedItem);
            this.set("isEditing", false);
            this.set("editingItemId", undefined);
            this.set("submitButtonText", "+");
            UIHelper.showToast("Elemento actualizado");
        } else {
            const newItem: Item = {
                name: this.newItemName,
                description: `Persistencia: ${this.isSqlMode ? 'SQL' : 'NoSQL'}`
            };
            await this.currentRepo.create(newItem);
            UIHelper.showToast("Elemento añadido");
        }

        this.set("newItemName", "");
        await this.refresh();
    }

    editItem = (args: any) => {
        const item = args.object.bindingContext as Item;
        if (item) {
            this.set("newItemName", item.name);
            this.set("isEditing", true);
            this.set("editingItemId", item.id);
            this.set("submitButtonText", "✓");
            UIHelper.showToast("Editando: " + item.name);
        }
    }

    cancelEdit = () => {
        this.set("isEditing", false);
        this.set("editingItemId", undefined);
        this.set("newItemName", "");
        this.set("submitButtonText", "+");
    }

    deleteItem = async (args: any) => {
        const item = args.object.bindingContext as Item;
        if (item && item.id !== undefined) {
            await this.currentRepo.delete(item.id);
            UIHelper.showToast("Elemento eliminado");
            await this.refresh();
        }
    }

    async refresh() {
        try {
            const data = await this.currentRepo.getAll();
            this.items.splice(0, this.items.length, ...data);
            Logger.debug(`Vista actualizada. Motor: ${this.isSqlMode ? 'SQL' : 'NoSQL'}. Total: ${data.length}`);
        } catch (e) {
            Logger.error("Error al refrescar la lista", e);
        }
    }
}
