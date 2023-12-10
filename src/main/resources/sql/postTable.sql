CREATE TABLE IF NOT EXISTS post(
id serial PRIMARY KEY,
title text,
link text UNIQUE,
description text,
created timestamp
);