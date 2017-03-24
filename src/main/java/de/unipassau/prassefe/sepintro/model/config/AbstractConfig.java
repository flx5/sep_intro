package de.unipassau.prassefe.sepintro.model.config;

import java.util.Optional;

import javax.sql.DataSource;

import de.unipassau.prassefe.sepintro.model.repository.Repository;
import de.unipassau.prassefe.sepintro.model.repository.RepositoryNotFoundException;
import de.unipassau.prassefe.sepintro.util.EnumUtil;

/**
 * The configuration base.
 *
 * @author Felix Prasse <prassefe@fim.uni-passau.de>
 */
public abstract class AbstractConfig {

    private Backend backend;
    private DataSource dataSource;

    /**
     * Create a new autoloading configuration.
     */
    public AbstractConfig() {
        this(true);
    }

    /**
     * Create a new configuration.
     *
     * @param autoload Should this configuration automatically call
     * {@link AbstractConfig#reload()}
     */
    public AbstractConfig(boolean autoload) {
        if (autoload) {
            reload();
        }
    }

    /**
     * Set the backend to be used.
     *
     * @param backendName The name of the backend.
     */
    protected void setBackend(String backendName) {
        Optional<Backend> newBackend = EnumUtil.lookup(Backend.class, backendName);

        if (!newBackend.isPresent()) {
            throw new InvalidConfiguration("Invalid backend " + backendName);
        }

        setBackend(newBackend.get());
    }

    /**
     * Set the backend.
     *
     * @param newBackend The new value.
     */
    protected void setBackend(Backend newBackend) {
        this.backend = newBackend;
    }

    /**
     * Get the currently used backend.
     *
     * @return The current backend.
     */
    public Backend getBackend() {
        return backend;
    }

    /**
     * Get the currently used data source.
     *
     * @return The data source.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Set the new data source.
     *
     * @param value The new data source.
     */
    protected void setDataSource(DataSource value) {
        this.dataSource = value;
    }

    /**
     * Get the correct repository implementation by interface.
     *
     * @param <T> The interface type.
     * @param interfaceClass The interface class.
     * @return The implementing repository.
     */
    @SuppressWarnings("unchecked")
    public <T extends Repository<?, ?>> T getRepository(Class<T> interfaceClass) {
        try {
            T repository = (T) Class.forName(this.backend.getRepositoryPrefix() + interfaceClass.getSimpleName()).newInstance();
            repository.setConfig(this);
            return repository;

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RepositoryNotFoundException(e);
        }
    }

    /**
     * Reload the configuration.
     */
    public abstract void reload();
}
