package com.newsapp.aavaaz.providers.permissionprovider;

import android.content.Context;

import com.newsapp.aavaaz.mocks.MockPermissionProvider;
import com.newsapp.aavaaz.view.ContextProcessor;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class PermissionProviderTest {

    private static final String[] REQUIRED_PERMISSIONS = new String[]{"really_important_permission"};

    @Rule public ExpectedException expectedException = ExpectedException.none();
    @Mock ContextProcessor contextProcessor;
    @Mock Context context;

    private MockPermissionProvider permissionProvider;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        permissionProvider = new MockPermissionProvider(REQUIRED_PERMISSIONS, null);
        permissionProvider.setContextProcessor(contextProcessor);
    }

    @Test
    public void creatingInstanceWithNoRequiredPermissionShouldThrowException() {
        expectedException.expect(IllegalStateException.class);
        permissionProvider = new MockPermissionProvider(new String[]{}, null);
    }

    @Test
    public void whenThereIsNoContextHasPermissionShouldReturnFalse() {
        assertThat(permissionProvider.hasPermission()).isFalse();
    }

    @Test
    public void whenThereIsContextHasPermissionShouldReturnTrue() {
        when(contextProcessor.getContext()).thenReturn(context);
        permissionProvider.grantPermission(true);

        assertThat(permissionProvider.hasPermission()).isTrue();
    }

}