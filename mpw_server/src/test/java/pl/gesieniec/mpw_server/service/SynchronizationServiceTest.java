package pl.gesieniec.mpw_server.service;

import org.junit.jupiter.api.Test;

class SynchronizationServiceTest {

    @Test
    void shouldReturnAllUserFiles(){

        SynchronizationService synchronizationService = new SynchronizationService();
        synchronizationService.getAllUserStoredFiles("PRZEMEK");

    }

}