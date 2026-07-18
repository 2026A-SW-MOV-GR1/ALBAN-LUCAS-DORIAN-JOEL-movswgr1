import { EventData, Page, View } from "@nativescript/core";
import { MainViewModel } from "../view-models/main-view-model";

export function onNavigatingTo(args: EventData) {
    const page = <Page>args.object;
    if (!page.bindingContext) {
        page.bindingContext = new MainViewModel();
    }
}

// Función Proxy para el botón Eliminar
export function onDeleteItem(args: any) {
    const button = args.object as View;
    const vm = button.page.bindingContext as MainViewModel;
    if (vm) vm.deleteItem(args);
}

// Función Proxy para el botón Editar
export function onEditItem(args: any) {
    const view = args.object as View;
    const vm = view.page.bindingContext as MainViewModel;
    if (vm) vm.editItem(args);
}

// Función Proxy para cancelar edición
export function onCancelEdit(args: any) {
    const view = args.object as View;
    const vm = view.page.bindingContext as MainViewModel;
    if (vm) vm.cancelEdit();
}
