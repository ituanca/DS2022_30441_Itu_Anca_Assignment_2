package ro.tuc.ds2022.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.tuc.ds2022.entities.Device;
import ro.tuc.ds2022.entities.HourlyEnergyConsumption;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface HourlyEnergyConsumptionRepository extends JpaRepository<HourlyEnergyConsumption,Integer> {

    Optional<HourlyEnergyConsumption> findByTimestamp(LocalDateTime timestamp);

    List<HourlyEnergyConsumption> findByDevice(Device device);

    @Query(value = "SELECT h " +
            "FROM HourlyEnergyConsumption h " +
            "WHERE h.device = :device " +
            "AND h.timestamp = :timestamp")
    Optional<HourlyEnergyConsumption> findByTimestampAndDevice(LocalDateTime timestamp, Device device);

    @Query(value = "SELECT h " +
            "FROM HourlyEnergyConsumption h " +
            "WHERE h.device = :device " +
            "AND year(h.timestamp) = year(:date) " +
            "AND month(h.timestamp) = month(:date) " +
            "AND day(h.timestamp) = day(:date)")
    List<HourlyEnergyConsumption> findByDateAndDevice(Date date, Device device);

    @Query(value = "SELECT h " +
            "FROM HourlyEnergyConsumption h " +
            "WHERE h.device = :device " +
            "AND year(h.timestamp) = year(:date) " +
            "AND month(h.timestamp) = month(:date) " +
            "AND day(h.timestamp) = day(:date)" +
            "AND hour(h.timestamp) = :hour")
    List<HourlyEnergyConsumption> findListByDateAndHourAndDevice(Date date, Integer hour, Device device);

    @Query(value = "SELECT h " +
            "FROM HourlyEnergyConsumption h " +
            "WHERE h.device = :device " +
            "AND year(h.timestamp) = year(:date) " +
            "AND month(h.timestamp) = month(:date) " +
            "AND day(h.timestamp) = day(:date)" +
            "AND hour(h.timestamp) = :hour")
    HourlyEnergyConsumption findByDateAndHourAndDevice(LocalDate date, Integer hour, Device device);
}
