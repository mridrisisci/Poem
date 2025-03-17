package app.daos;

import app.config.HibernateConfig;
import app.entities.Account;
import app.entities.Role;
import app.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/*

Purpose: This DAO is used for the Security Layer
Description:
 */

public class SecurityDAO
{
    private GenericDAO genericDAO;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private static SecurityDAO instance;
    private Logger logger = LoggerFactory.getLogger(SecurityDAO.class);

    public SecurityDAO(EntityManagerFactory emf)
    {
        genericDAO = GenericDAO.getInstance(emf);

    }

    public static SecurityDAO getInstance(EntityManagerFactory emf)
    {
        if (instance == null)
        {
            instance = new SecurityDAO(emf);
        }
        return instance;
    }

    public Account create(Account account)
    {
        try (var em = emf.createEntityManager())
        {
            Set<Role> newRoleSet = new HashSet<>();
            if (account.getRoles().size() == 0)
            {
                Role accountRole = em.find(Role.class, "account");
                if (accountRole == null)
                {
                    accountRole = new Role("account");
                    em.persist(accountRole);
                }
                account.addRole(accountRole);
            }
            account.getRoles().forEach(role ->
            {
                Role foundRole = em.find(Role.class, role.getName());
                if (foundRole == null)
                {
                    throw new EntityNotFoundException("no role found with this id");
                } else
                {
                    newRoleSet.add(foundRole);
                }
            });
            account.setRoles(newRoleSet);
            em.getTransaction().begin();
            em.persist(account);
            em.getTransaction().commit();
        } catch (Exception e)
        {
            logger.error("Error creating account: " + e.getMessage());
            return null;
        }
        return account;
    }

    public Role createRole(Role role)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(role);
            em.getTransaction().commit();
            return role;
        }
    }

    public UserDTO getVerifiedAccount(String username, String password) throws ValidationException
    {
        try (EntityManager em = emf.createEntityManager())
        {
            Account account = em.find(Account.class, username);
            if (account == null)
            {
                throw new EntityNotFoundException("User not found" + username);
            }
            account.getRoles().size();
            if (!account.verifyPassword(password))
            {
                throw new ValidationException("Wrong password");
            }
            return new UserDTO(account.getUsername(), account.getRoles().stream()
                .map(r -> r.getName()).collect(Collectors.toSet()));
        }
    }

    public Account updateAccount(Account account)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.merge(account);
            em.getTransaction().commit();
            return account;
        }
    }

    public Account deleteAccount (Account account)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.remove(account);
            em.getTransaction().commit();
            return account;
        }
    }


}
