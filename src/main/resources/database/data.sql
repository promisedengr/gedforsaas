-- **************** MYSQL REQUEST ****************
-- INSERT IGNORE INTO role (role_id, name) VALUES
--   ('1', 'ROLE_SUPER_ADMIN'),
--   ('2', 'ROLE_ADMIN'),
--   ('3', 'ROLE_USER');
-- --
-- **************** POSTGRESQL REQUEST ****************
insert into role (role_id, name) values
  ('1', 'ROLE_SUPER_ADMIN'),
  ('2', 'ROLE_ADMIN'),
  ('3', 'ROLE_USER')
  on conflict (role_id) DO NOTHING;

INSERT INTO acl_class(id, class) VALUES
  ('1', 'ma.project.GedforSaas.folder.Folder'),
  ('2', 'ma.project.GedforSaas.document.Document')
  on conflict (id) DO NOTHING;
