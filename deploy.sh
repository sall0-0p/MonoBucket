set -e

cd /root/projects/bucketbank

git pull origin main
docker compose build
docker compose down
docker compose up -d
docker image prune -f

echo "Deployed"