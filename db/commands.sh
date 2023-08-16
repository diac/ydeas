docker build -t postgresql_ydeas ./
docker run -itd -p 5432:5432 -v ~/postgres_data:/var/lib/postgresql/data --name postgresql_ydeas postgresql_ydeas