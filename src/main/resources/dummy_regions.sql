-- Dummy region data for testing
-- This file contains a set of nested, rectangular polygons to simulate Korean administrative districts.
-- All polygons are designed to be adjacent as requested.

-- Clear existing data
DELETE FROM region;

-- Level 1: 시/도 (Provinces/Cities)
INSERT INTO region (region_name, region_depth1, geom) VALUES
('경기도', '경기도', ST_GeomFromText('POLYGON((0 0, 50 0, 50 100, 0 100, 0 0))', 4326)),
('서울특별시', '서울특별시', ST_GeomFromText('POLYGON((50 0, 100 0, 100 100, 50 100, 50 0))', 4326));

-- Level 2: 시/군/구 (Districts)
-- Inside 경기도 (0-50 X, 0-100 Y)
INSERT INTO region (region_name, region_depth1, region_depth2, geom) VALUES
('안양시', '경기도', '안양시', ST_GeomFromText('POLYGON((0 0, 50 0, 50 50, 0 50, 0 0))', 4326)),
('과천시', '경기도', '과천시', ST_GeomFromText('POLYGON((0 50, 50 50, 50 100, 0 100, 0 50))', 4326));
-- Inside 서울특별시 (50-100 X, 0-100 Y)
INSERT INTO region (region_name, region_depth1, region_depth2, geom) VALUES
('성북구', '서울특별시', '성북구', ST_GeomFromText('POLYGON((50 0, 100 0, 100 50, 50 50, 50 0))', 4326)),
('동대문구', '서울특별시', '동대문구', ST_GeomFromText('POLYGON((50 50, 100 50, 100 100, 50 100, 50 50))', 4326));

-- Level 3: 행/정/동 (Neighborhoods)
-- Inside 안양시 (0-50 X, 0-50 Y)
INSERT INTO region (region_name, region_depth1, region_depth2, region_depth3, geom) VALUES
('안양1동', '경기도', '안양시', '안양1동', ST_GeomFromText('POLYGON((0 0, 25 0, 25 50, 0 50, 0 0))', 4326)),
('안양2동', '경기도', '안양시', '안양2동', ST_GeomFromText('POLYGON((25 0, 50 0, 50 50, 25 50, 25 0))', 4326));
-- Inside 과천시 (0-50 X, 50-100 Y)
INSERT INTO region (region_name, region_depth1, region_depth2, region_depth3, geom) VALUES
('과천1동', '경기도', '과천시', '과천1동', ST_GeomFromText('POLYGON((0 50, 25 50, 25 100, 0 100, 0 50))', 4326)),
('과천2동', '경기도', '과천시', '과천2동', ST_GeomFromText('POLYGON((25 50, 50 50, 50 100, 25 100, 25 50))', 4326));
-- Inside 성북구 (50-100 X, 0-50 Y)
INSERT INTO region (region_name, region_depth1, region_depth2, region_depth3, geom) VALUES
('성북1동', '서울특별시', '성북구', '성북1동', ST_GeomFromText('POLYGON((50 0, 75 0, 75 50, 50 50, 50 0))', 4326)),
('성북2동', '서울특별시', '성북구', '성북2동', ST_GeomFromText('POLYGON((75 0, 100 0, 100 50, 75 50, 75 0))', 4326));
-- Inside 동대문구 (50-100 X, 50-100 Y)
INSERT INTO region (region_name, region_depth1, region_depth2, region_depth3, geom) VALUES
('동대문1동', '서울특별시', '동대문구', '동대문1동', ST_GeomFromText('POLYGON((50 50, 75 50, 75 100, 50 100, 50 50))', 4326)),
('동대문2동', '서울특별시', '동대문구', '동대문2동', ST_GeomFromText('POLYGON((75 50, 100 50, 100 100, 75 100, 75 50))', 4326));

-- Create spatial index after inserting data
CREATE SPATIAL INDEX IF NOT EXISTS region_geom_idx ON region(geom);
