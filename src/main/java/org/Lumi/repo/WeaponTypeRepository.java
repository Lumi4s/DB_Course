package org.Lumi.repo;

import org.Lumi.model.WeaponType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeaponTypeRepository extends JpaRepository<WeaponType, Integer> {
}

