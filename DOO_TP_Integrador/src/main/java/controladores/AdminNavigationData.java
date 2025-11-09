package controladores;

import dto.ClienteDto;

/**
 * Utility class to handle navigation data between admin screens
 * when using static navigation to preserve the MenuBar with logout button
 */
public class AdminNavigationData {
    
    private static ClienteDto clienteForEdit;
    private static boolean editMode = false;
    
    // Getters
    public static ClienteDto getClienteForEdit() {
        return clienteForEdit;
    }
    
    public static boolean isEditMode() {
        return editMode;
    }
    
    // Setters
    public static void setClienteForEdit(ClienteDto cliente) {
        clienteForEdit = cliente;
        editMode = true;
    }
    
    public static void setAddMode() {
        clienteForEdit = null;
        editMode = false;
    }
    
    // Utility methods
    public static void clearAll() {
        clienteForEdit = null;
        editMode = false;
    }
    
    public static boolean hasClienteForEdit() {
        return clienteForEdit != null && editMode;
    }
}
